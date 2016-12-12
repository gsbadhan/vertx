package com.http;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;

public class HttpServer {

	public static void main(String args[]){
		Vertx.factory.vertx().createHttpServer().requestHandler(new Handler<HttpServerRequest>() {
			
			public void handle(HttpServerRequest request) {
				request.response().end("started..");
				
			}
		}).listen(8090);
	}
}
