package top.anets.module.shop.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.anets.exception.ServiceException;
import top.anets.module.shop.service.ShopService;
import top.anets.support.advice.Response;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ftm
 * @date 2024/5/29 0029 22:53
 */
@RestController
@RequestMapping("shop")
public class ShopController {

    @Resource
    private ShopService shopService;

    @Response
    @RequestMapping("list")
    public JSONObject list(@RequestBody List<String> ids){
        if(CollectionUtils.isEmpty(ids)){
            return null;
        }
        if(ids.size()>100){
            throw new ServiceException("每次查询不超过100个商品");
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("product_list", shopService.list(ids));
        return jsonObject;
    }
}
