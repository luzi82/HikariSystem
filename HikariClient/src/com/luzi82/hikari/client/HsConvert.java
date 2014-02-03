package com.luzi82.hikari.client;

public interface HsConvert<O, I> {

	public O convert(I i) throws Exception;

}
