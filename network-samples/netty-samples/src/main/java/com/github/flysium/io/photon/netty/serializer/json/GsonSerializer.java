/*
 * Copyright 2020 SvenAugustus
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.flysium.io.photon.netty.serializer.json;

import com.github.flysium.io.photon.netty.serializer.Serializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Gson Serializer.
 *
 * @author Sven Augustus
 * @version 1.0
 */
public class GsonSerializer implements Serializer {

  private final Gson gson = new GsonBuilder().create();

  @Override
  public String name() {
    return "Gson";
  }

  @Override
  public <T> byte[] serialize(T object) throws Exception {
    return gson.toJson(object).getBytes();
  }

  @Override
  public <T> T deserialize(byte[] data, Class<T> type) throws Exception {
    return gson.fromJson(new String(data), type);
  }

}