package com.luzi82.hikari.client;

import org.apache.commons.lang.ObjectUtils;

import com.luzi82.hikari.client.protocol.ResourceProtocol;

public class Resource extends ResourceProtocol {

	public static int getCount(Status resourceStatus0, String resource_key) {
		for (ResourceKeyCount rkc : resourceStatus0.resourceKeyCountList) {
			if (ObjectUtils.equals(rkc.resource_key, resource_key)) {
				return rkc.count;
			}
		}
		return 0;
	}

}
