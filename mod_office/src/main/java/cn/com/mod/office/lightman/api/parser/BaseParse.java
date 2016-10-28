package cn.com.mod.office.lightman.api.parser;

import android.os.Build;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;
import java.lang.reflect.Modifier;

import cn.com.mod.office.lightman.api.BaseResp;

/**
 * @MetohdName:
 *
 * @Description:TODO(这里用一句话描述这个类的作用)
 *
 * @author: weinong
 *
 * @date: 2015年11月17日
 * 
 */
public class BaseParse<T extends BaseResp> {

	public T parse(String jsonString, Class<?> clazz) throws JsonIOException, JsonSyntaxException {
		Gson gson;
		final int sdk = Build.VERSION.SDK_INT;
		if (sdk >= 23) {
			GsonBuilder gsonBuilder = new GsonBuilder().excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT,
					Modifier.STATIC);
			gson = gsonBuilder.create();
		} else {
			gson = new Gson();
		}
		JsonReader reader = new JsonReader(new StringReader(jsonString));
		reader.setLenient(true);

		return gson.fromJson(reader, clazz);

	}

}
