package com.http.products;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.ext.web.Router;

public class HttpServer extends AbstractVerticle {

	public HttpServer() {
		System.out.println("HttpServer instance created..");
	}

	@Override
	public void start(Future<Void> startFuture) throws Exception {
		System.out.println("start HttpServer verticle...");
		
		Router router = Router.router(vertx);
		Routes.buildRoutes(router,vertx);
		initHttpServer(router, startFuture);
		
		
		vertx.deployVerticle(new ProductsService());
	}


	private void initHttpServer(Router router, Future<Void> startFuture) {
		int port = 9078;
		vertx.createHttpServer().requestHandler(router).listen(port, "127.0.0.1", finalHandler -> {
			if (finalHandler.succeeded()) {
				startFuture.complete();
			} else {
				startFuture.fail(finalHandler.cause());
			}
		});
		System.out.println("HTTP server started at:" + port);
	}

}
