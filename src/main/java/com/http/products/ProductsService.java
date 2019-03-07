package com.http.products;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.common.utils.Util;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class ProductsService extends AbstractVerticle {
	protected static final String END_PRODUCT_LIST = "endProductList";
	protected static final String END_PRODUCT_BY_ID = "endProductById";
	protected static final String END_PRODUCT_CREATE = "endProductCreate";
	protected static final String END_PRODUCT_DELETE = "endProductDelete";
	private static final Map<String, Product> products = new ConcurrentHashMap<String, Product>(50);

	public ProductsService() {
		System.out.println("start ProductsService instance...");
	}

	@Override
	public void start() throws Exception {
		System.out.println("start ProductsService verticle...");
		Util.printWorker("start-worker");

		vertx.eventBus().consumer(END_PRODUCT_LIST, hnd -> {
			Util.printWorker("router");
			hnd.reply(allProducts());
		});

		vertx.eventBus().consumer(END_PRODUCT_BY_ID, hnd -> {
			Util.printWorker("router");
			hnd.reply(productById(hnd.body().toString()));
		});

		vertx.eventBus().consumer(END_PRODUCT_CREATE, hnd -> {
			Util.printWorker("router");
			hnd.reply(createProduct((JsonObject) hnd.body()));
		});

		vertx.eventBus().consumer(END_PRODUCT_DELETE, hnd -> {
			Util.printWorker("router");
			hnd.reply(deleteProductById(hnd.body().toString()));
		});
	}

	private JsonObject deleteProductById(String id) {
		Util.printWorker("delete");
		JsonObject resp = new JsonObject();
		Product product = products.remove(id);
		if (product != null) {
			resp.put("status", "product deleted " + id);
		} else {
			resp.put("status", "product not exist " + id);
		}
		return resp;
	}

	private JsonObject createProduct(JsonObject product) {
		Util.printWorker("create");
		String id = product.getString("id");
		Product newProduct = new Product(product.getString("name"), product.getString("id"),
				product.getDouble("price"));
		products.put(id, newProduct);
		return product;
	}

	public JsonObject allProducts() {
		Util.printWorker("allList");
		JsonArray jsonArray = new JsonArray();
		products.values().forEach(v -> {
			jsonArray.add(Json.encode(v));
		});
		JsonObject object = new JsonObject();
		object.put("products", jsonArray);
		return object;
	}

	public JsonObject productById(String id) {
		Util.printWorker("getproduct");
		JsonObject object = new JsonObject();
		object.put("products", Json.encode(products.get(id)));
		return object;
	}
}
