/*
 * Copyright (C) 2019 Bukkit Commons
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.colormotd.utils;

import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IpAddressManager {
    private HashMap<String,IpAddressInfo> cacheMap = new HashMap<>();
    private Config config;
    private Gson gson = new Gson();
    final String taobaoProvider = "https://ip.taobao.com/outGetIpInfo";


    public IpAddressManager(Config config){
        this.config = config;
    }

    public IpAddressInfo getIpInfomations(String ip){
        if(cacheMap.containsKey(ip)) {
            return cacheMap.get(ip); //返回缓存数据
        }
        IpAddressInfo info = null;
        if ("taobao".equals(config.getIpProvider())) {
            String json = HttpUtils.doPost(taobaoProvider ,ip);
            try {
                info = gson.fromJson(json, TaobaoIpProvider.class);
            } catch (JsonSyntaxException ex) {
                //Ignore
                info = TaobaoIpProvider.builder().code(404).data(TaobaoIpProvider.TaobaoIpData.builder().area("未知").city("未知").country("未知").ip(ip).region("未知").isp("未知").build()).build();
                System.out.println("Json Syntax Exception founded: " + json + "\n" + ex.getMessage());
            }
        }
        if(cacheMap.size() > 3000) {
            cacheMap.clear(); //清空缓存
        }
        cacheMap.put(ip, info);
        //放置缓存
        return info;
    }
}
