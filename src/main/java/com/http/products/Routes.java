package com.http.products;

import java.util.HashMap;
import java.util.Map;

import io.netty.util.internal.StringUtil;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Cookie;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.sstore.ClusteredSessionStore;

public class Routes {
	public static final String SESSION_MAP_NAME = "sessionMapName";
	public static final String SESSION_LOGIN_KEY = "loginKey";
	public static final long SESSION_TIMEOUT = (5 * 60 * 1000);

	private Routes() {
	}

	public static void buildRoutes(Router router, Vertx vertx) {
		//set body handler
		router.route().handler(BodyHandler.create());
		// set cookie handler for all APIs
		router.route().handler(CookieHandler.create());
		
		// session for user's aliveness
//		ClusteredSessionStore sessionStore = ClusteredSessionStore.create(vertx, SESSION_MAP_NAME, SESSION_TIMEOUT);
//		SessionHandler sessionHandler = SessionHandler.create(sessionStore);
//		router.route().handler(sessionHandler);

		// get product list
		router.get("/product/list/").handler(rh -> {
			logRouteMeta("productList", rh);
			vertx.eventBus().send(ProductsService.END_PRODUCT_LIST, null, replyHand -> {
				JsonObject body = (JsonObject) replyHand.result().body();
				response(rh, body);
			});
		});

		// get product
		router.get("/product/:productid/").handler(rh -> {
			logRouteMeta("productGetById", rh);
			String id = rh.pathParam("productid");
			vertx.eventBus().send(ProductsService.END_PRODUCT_BY_ID, id, replyHand -> {
				JsonObject body = (JsonObject) replyHand.result().body();
				response(rh, body);
			});
		});

		// create product
		router.put("/product/create/").consumes("application/json").handler(rh -> {
			logRouteMeta("productCreate", rh);
			JsonObject product = rh.getBodyAsJson();
			vertx.eventBus().send(ProductsService.END_PRODUCT_CREATE, product, replyHand -> {
				JsonObject body = (JsonObject) replyHand.result().body();
				response(rh, body);
			});
		});
		
		//delete product
		router.delete("/product/:productid/").handler(rh -> {
			logRouteMeta("productDeleteById", rh);
			String id = rh.pathParam("productid");
			vertx.eventBus().send(ProductsService.END_PRODUCT_DELETE, id, replyHand -> {
				JsonObject body = (JsonObject) replyHand.result().body();
				response(rh, body);
			});
		});
	}

	private static void response(RoutingContext rh, JsonObject body) {
		rh.addCookie(Cookie.cookie("testCooky", "testValue"));
		rh.response().putHeader("Content-Type", "application/json").end(body.encodePrettily());
	}

	private static void logRouteMeta(String vid, RoutingContext rh) {
		System.out.println("request:" + vid + ":" + rh.request().absoluteURI());
		System.out.println("request:" + vid + ":" + rh.request().uri());
		Map<String, String> cookies = new HashMap<String, String>();
		rh.cookies().forEach(c -> cookies.put(c.getName(), c.getValue()));
		System.out.println("request cookies:" + vid + ":" + cookies);
		System.out.println("request session:" + vid + ":" + rh.session());
	}
}
