package com.common.utils;

public class Util {

	private Util() {
	}

	public static final void printWorker(String... id) {
		if (id == null) {
			System.out.println(
					"worker info : " + Thread.currentThread().getName() + " : " + Thread.currentThread().getId());
		} else {
			System.out.println("method: " + id[0] + " worker info : " + Thread.currentThread().getName() + " : "
					+ Thread.currentThread().getId());
		}
	}

}
