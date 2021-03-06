package com.luzi82.hikari.client.protocol.gen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.luzi82.hikari.client.protocol.Item;

public class Main {

	public static final String[] INPUT_PATH_LIST = { "../HikariProtocolDef/src" };

	public static final String OUTPUT = "../HikariClientProtocol/genSrc";

	public static final String JAVA_POFIX = ".java";
	public static final String PROTOCOLDEF_POFIX = "ProtocolDef";
	public static final String PROTOCOL_POFIX = "Protocol";

	public void m(String[] argv) throws Exception {
		Velocity.init();

		List<Data> dataLoadList = new LinkedList<Data>();
		List<Status> statusList = new LinkedList<Status>();

		Template template = Velocity.getTemplate("vm/out.vm");

		File outputFile = new File(OUTPUT);
		FileUtils.deleteDirectory(outputFile);
		outputFile.mkdirs();

		for (String inputPath : INPUT_PATH_LIST) {
			File f = new File(inputPath);
			String fAbs = f.getPath();

			Iterator<File> fileItr = FileUtils.iterateFiles(f, null, true);
			while (fileItr.hasNext()) {
				File ff = fileItr.next();
				if (!ff.isFile())
					continue;
				if (ff.isDirectory())
					continue;
				if (!ff.getName().endsWith(PROTOCOLDEF_POFIX + JAVA_POFIX))
					continue;
				String c = ff.getPath();
				c = c.substring(fAbs.length() + 1, c.length());
				c = c.substring(0, c.length()
						- (PROTOCOLDEF_POFIX + JAVA_POFIX).length());
				String cn = c.replaceAll(Pattern.quote("/"), ".");
				Class<?> cls = Class.forName(cn + PROTOCOLDEF_POFIX);

				int lastDotIdx = cn.lastIndexOf(".");
				String mainName = cn.substring(lastDotIdx + 1, cn.length());

				String[] mainNameS = mainName.split("(?<!^)(?=[A-Z])");
				for (int i = 0; i < mainNameS.length; ++i) {
					mainNameS[i] = mainNameS[i].toLowerCase();
				}
				String appName = StringUtils.join(mainNameS, "_");

				File out = new File(outputFile, c + "Protocol.java");
				File outDir = out.getParentFile();
				outDir.mkdirs();

				VelocityContext vc = new VelocityContext();
				vc.put("main", mainName);
				vc.put("app_name", appName);

				LinkedList<Cmd> cmdList = new LinkedList<Main.Cmd>();
				for (Class<?> cmdClass : cls.getDeclaredClasses()) {
					try {
						Cmd cmd = new Cmd();
						if (!cmdClass.getSimpleName().endsWith("Cmd")) {
							continue;
						}
						cmd.cname = cmdClass.getSimpleName();
						String cmdName = cmd.cname.substring(0,
								cmd.cname.length() - 3);
						cmd.fname = cmdName.substring(0, 1).toLowerCase()
								+ cmdName.substring(1, cmdName.length());
						String[] ns = cmdName.split("(?<!^)(?=[A-Z])");
						for (int i = 0; i < ns.length; ++i) {
							ns[i] = ns[i].toLowerCase();
						}
						cmd.jname = StringUtils.join(ns, "_");

						Class<?> reqClass = Class.forName(cmdClass.getName()
								+ "$Request");
						for (Field field : reqClass.getFields()) {
							Fieldd fd = new Fieldd();
							fd.name = field.getName();
							fd.type = field.getType().getCanonicalName();
							cmd.reqList.add(fd);
						}
						Class<?> resClass = Class.forName(cmdClass.getName()
								+ "$Result");
						for (Field field : resClass.getFields()) {
							Fieldd fd = new Fieldd();
							fd.name = field.getName();
							fd.type = field.getType().getCanonicalName();
							cmd.resList.add(fd);
						}
						cmdList.add(cmd);
					} catch (ClassNotFoundException cnfe) {
						// do nothing
					}
				}
				vc.put("cmd_list", cmdList);

				LinkedList<Data> dataList = new LinkedList<Main.Data>();
				for (Class<?> dataClass : cls.getDeclaredClasses()) {
					Data data = new Data();
					if (!dataClass.getSimpleName().endsWith("Data")) {
						continue;
					}
					data.appName = appName;
					data.cnamefull = dataClass.getCanonicalName();
					data.cname = dataClass.getSimpleName();
					String dataName = data.cname.substring(0,
							data.cname.length() - 4);
					String[] ns = dataName.split("(?<!^)(?=[A-Z])");
					for (int i = 0; i < ns.length; ++i) {
						ns[i] = ns[i].toLowerCase();
					}
					data.jname = StringUtils.join(ns, "_");

					dataList.add(data);
					dataLoadList.add(data);
				}
				vc.put("data_list", dataList);

				// boolean statusExist = false;
				List<Status> statusList0 = new LinkedList<Status>();
				for (Class<?> dataClass : cls.getDeclaredClasses()) {
					String classSimpleName = dataClass.getSimpleName();
					if (!classSimpleName.endsWith("Status"))
						continue;
					// statusExist = true;
					Status status = new Status();
					status.appName = appName;
					status.jname = camelToLower(classSimpleName.substring(0,
							classSimpleName.length() - 6));
					status.cname = dataClass.getSimpleName();
					status.cnamefull = dataClass.getCanonicalName();
					statusList0.add(status);
					statusList.add(status);
				}
				// vc.put("status_exist", statusExist);
				vc.put("status_list", statusList0);

				List<Itemm> itemmList = new LinkedList<Itemm>();
				for (Class<?> dataClass : cls.getDeclaredClasses()) {
					if (!Item.class.isAssignableFrom(dataClass))
						continue;
					String classSimpleName = dataClass.getSimpleName();
					if (!classSimpleName.endsWith("Item"))
						continue;
					// statusExist = true;
					Itemm itemm = new Itemm();
					itemm.appName = appName;
					itemm.jname = camelToLower(classSimpleName.substring(0,
							classSimpleName.length() - 4));
					itemm.cname = dataClass.getSimpleName();
					itemm.cnamefull = dataClass.getCanonicalName();

					for (Field field : dataClass.getFields()) {
						Fieldd fd = new Fieldd();
						fd.name = field.getName();
						fd.type = field.getType().getCanonicalName();
						itemm.fieldList.add(fd);
					}

					itemmList.add(itemm);
				}
				vc.put("item_list", itemmList);

				BufferedWriter bw = new BufferedWriter(new FileWriter(out));
				template.merge(vc, bw);

				bw.flush();
				IOUtils.closeQuietly(bw);
			}
		}

		Template clientInitTemplate = Velocity.getTemplate("vm/clientInit.vm");

		File clientInitFile = new File(outputFile,
				"com/luzi82/hikari/client/protocol/gen/out/ClientInit.java");
		clientInitFile.getParentFile().mkdirs();

		VelocityContext clientInitVc = new VelocityContext();
		clientInitVc.put("status_list", statusList);
		clientInitVc.put("dataload_list", dataLoadList);

		BufferedWriter clientInitBw = new BufferedWriter(new FileWriter(
				clientInitFile));
		clientInitTemplate.merge(clientInitVc, clientInitBw);
		clientInitBw.flush();
		IOUtils.closeQuietly(clientInitBw);
	}

	public static class Cmd {
		public String cname;
		public String fname;
		public String jname;
		public LinkedList<Fieldd> reqList = new LinkedList<Fieldd>();
		public LinkedList<Fieldd> resList = new LinkedList<Fieldd>();

		public String getCname() {
			return cname;
		}

		public String getFname() {
			return fname;
		}

		public String getJname() {
			return jname;
		}

		public LinkedList<Fieldd> getReqList() {
			return reqList;
		}

		public LinkedList<Fieldd> getResList() {
			return resList;
		}
	}

	public static class Fieldd {
		public String name;
		public String type;

		public String getName() {
			return name;
		}

		public String getType() {
			return type;
		}
	}

	public static class Data {
		public String appName;
		public String cname;
		public String cnamefull;
		public String jname;

		public String getAppName() {
			return appName;
		}

		public String getCname() {
			return cname;
		}

		public String getJname() {
			return jname;
		}

		public String getCnamefull() {
			return cnamefull;
		}
	}

	public static class Status {
		public String appName;
		public String cnamefull;
		public String cname;
		public String jname;

		public String getAppName() {
			return appName;
		}

		public String getJname() {
			return jname;
		}

		public String getCname() {
			return cname;
		}

		public String getCnamefull() {
			return cnamefull;
		}
	}

	public static class Itemm {
		public String appName;
		public String cnamefull;
		public String cname;
		public String jname;
		public LinkedList<Fieldd> fieldList = new LinkedList<Fieldd>();

		public String getAppName() {
			return appName;
		}

		public String getJname() {
			return jname;
		}

		public String getCname() {
			return cname;
		}

		public String getCnamefull() {
			return cnamefull;
		}

		public LinkedList<Fieldd> getFieldList() {
			return fieldList;
		}
	}

	public static void main(String[] argv) throws Exception {
		Main main = new Main();
		main.m(argv);
	}

	private static String camelToLower(String in) {
		String[] ns = StringUtils.splitByCharacterTypeCamelCase(in);
		for (int i = 0; i < ns.length; ++i) {
			ns[i] = ns[i].toLowerCase();
		}
		return StringUtils.join(ns, "_");
	}

}
