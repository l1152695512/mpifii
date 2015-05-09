
package com.yinfu.service.client;

public class HelloClient {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestWebsServiceService myService = new TestWebsServiceService();
		TestWebsService ms = myService.getTestWebsServicePort();
		String s = ms.sayHello("why");
		System.out.println(s);
	}
}
