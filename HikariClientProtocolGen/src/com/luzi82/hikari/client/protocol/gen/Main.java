package com.luzi82.hikari.client.protocol.gen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

public class Main {

	public static final String[] INPUT = { "../HikariProtocolDef/src" };

	public static final String OUTPUT = "../HikariClientEndpoint/genSrc";

	public static final String JAVA_POFIX = ".java";
	public static final String PROTOCOLDEF_POFIX = "ProtocolDef";
	public static final String PROTOCOL_POFIX = "Protocol";

	public void m(String[] argv) throws Exception {
		Velocity.init();

		Template template = Velocity.getTemplate("vm/out.vm");

		File outputFile = new File(OUTPUT);
		FileUtils.deleteDirectory(outputFile);
		outputFile.mkdirs();

		for (String inputPath : INPUT) {
			File f = new File(inputPath);
			Path fAbs = f.getAbsoluteFile().toPath().normalize();
			f = fAbs.toFile();
			System.err.println(fAbs.toString());
			Iterator<File> fileItr = FileUtils.iterateFiles(f, null, true);
			while (fileItr.hasNext()) {
				File ff = fileItr.next();
				if (!ff.isFile())
					continue;
				if (ff.isDirectory())
					continue;
				if (!ff.getName().endsWith(PROTOCOLDEF_POFIX + JAVA_POFIX))
					continue;
				String c = fAbs.relativize(ff.toPath()).toString();
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
					Cmd cmd = new Cmd();
					cmd.cname = cmdClass.getSimpleName();
					cmd.fname = cmd.cname.substring(0, 1).toLowerCase()
							+ cmd.cname.substring(1, cmd.cname.length());
					String[] ns = cmd.cname.split("(?<!^)(?=[A-Z])");
					for (int i = 0; i < ns.length; ++i) {
						ns[i] = ns[i].toLowerCase();
					}
					cmd.jname = StringUtils.join(ns, "_");

					Class<?> reqClass = Class.forName(cmdClass.getName()
							+ "$Request");
					for (Field field : reqClass.getFields()) {
						Fieldd fd = new Fieldd();
						fd.name = field.getName();
						fd.type = field.getType().getName();
						cmd.reqList.add(fd);
					}
					Class<?> resClass = Class.forName(cmdClass.getName()
							+ "$Result");
					for (Field field : resClass.getFields()) {
						Fieldd fd = new Fieldd();
						fd.name = field.getName();
						fd.type = field.getType().getName();
						cmd.resList.add(fd);
					}
					cmdList.add(cmd);
				}
				vc.put("cmd_list", cmdList);

				// StringWriter sw = new StringWriter();
				BufferedWriter bw = new BufferedWriter(new FileWriter(out));
				template.merge(vc, bw);

				// System.err.println(sw.toString());

				//
				//
				// bw.write("package ");
				// bw.write(pkgName);
				// bw.write(";");
				// bw.newLine();
				//
				// bw.write("import java.util.concurrent.Future;");
				// bw.newLine();
				// bw.write("import org.apache.http.concurrent.FutureCallback;");
				// bw.newLine();
				// bw.write("import com.luzi82.hikari.client.endpoint.HsCmdManager;");
				// bw.newLine();
				//
				// bw.write("public class ");
				// bw.write(className);
				// bw.write(" extends ");
				// bw.write(parentClassName);
				// bw.write(" {");
				// bw.newLine();
				//
				// bw.write("public static final String APP_NAME = \"");
				// bw.write(appName);
				// bw.write("\";");
				// bw.newLine();
				//
				// Class<?> cmdClassV[] = cls.getDeclaredClasses();
				// for (Class<?> cmdClass : cmdClassV) {
				// String cmdClassName = cmdClass.getSimpleName();
				// String cmdFuncName = cmdClassName.substring(0, 1)
				// .toLowerCase()
				// + cmdClassName.substring(1, cmdClassName.length());
				// Class<?> reqClass = Class.forName(cmdClass.getName()
				// + "$Request");
				// Class<?> resClass = Class.forName(cmdClass.getName()
				// + "$Result");
				// Field[] reqFieldV = reqClass.getFields();
				// Field[] resFieldV = resClass.getFields();
				// bw.write("public static Future<");
				// bw.write(cmdClassName);
				// bw.write(".Result> ");
				// bw.write(cmdFuncName);
				// bw.write("(final HsCmdManager cmdManager");
				// for (Field reqField : reqFieldV) {
				// bw.write(",");
				// bw.write(reqField.getType().getName());
				// bw.write(" ");
				// bw.write(reqField.getName());
				// }
				// bw.write(",final FutureCallback<");
				// bw.write(cmdClassName);
				// bw.write(".Result> futureCallback) {");
				// bw.newLine();
				// bw.write("}");
				// bw.newLine();
				// }
				//
				// bw.write("}");
				// bw.newLine();

				// System.err.println(cls.getName());
				// Field[] fieldV = cls.getFields();

				bw.flush();
				IOUtils.closeQuietly(bw);
			}
		}
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

	public static void main(String[] argv) throws Exception {
		Main main = new Main();
		main.m(argv);
	}

}
