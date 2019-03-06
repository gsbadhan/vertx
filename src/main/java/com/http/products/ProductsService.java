package com.http.products;

import java.util.HashMap;
import java.util.Map;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public class ProductsService extends AbstractVerticle {
	protected static final String END_PRODUCT_LIST = "endProductList";
	protected static final String END_PRODUCT_BY_ID = "endProductById";
	protected static final String END_PRODUCT_CREATE = "endProductCreate";

	public ProductsService() {
		System.out.println("start ProductsService instance...");
	}

	@Override
	public void start() throws Exception {
		System.out.println("start ProductsService verticle...");

		vertx.eventBus().consumer(END_PRODUCT_LIST, hnd -> {
			hnd.reply(allProducts());
		});

		vertx.eventBus().consumer(END_PRODUCT_BY_ID, hnd -> {
			hnd.reply(productById(hnd.body().toString()));
		});

		vertx.eventBus().consumer(END_PRODUCT_CREATE, hnd -> {
		});
	}

	public JsonObject allProducts() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("id", "1001");
		data.put("name", "shirt");
		data.put("price", 500);
		JsonObject object = new JsonObject(data);
		return object;
	}

	public JsonObject productById(String id) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("id", id);
		data.put("name", "t-shirt");
		data.put("price", 400);
		JsonObject object = new JsonObject(data);
		return object;
	}
}
