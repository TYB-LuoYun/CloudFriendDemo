package top.anets.module.shop.dao;

import cn.hutool.core.convert.Convert;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import top.anets.exception.ServiceException;
import top.anets.module.shop.model.Shop;
import top.anets.module.shop.model.ShopExcel;
import top.anets.module.shop.service.impl.ShopServiceImpl;
import top.anets.utils.DateUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author ftm
 * @date 2024/5/30 0030 0:36
 */
@Component
public class ShopDao {
    @Autowired
    private ResourceLoader resourceLoader;

    @SneakyThrows
    public Map<String, Shop> listByIds(List<String> ids) {
        //      解析
        List<ShopExcel> shopExcels = null;
        Resource resource = resourceLoader.getResource("classpath:商品数据.xlsx");
        try (InputStream in = resource.getInputStream()){
//          这个步骤，相当于查库(商品数据.xlsx可能会变化),有条件的，如有需要表格数据无变化，可预先全部加载到缓存;
            shopExcels = ShopDao.readExcel(in, ids);
            System.out.println(shopExcels.size());
        }catch (Exception e){
            e.printStackTrace();
            throw new ServiceException("解析表格异常");
        }
        //  表格数据按商品分组  sku->spu
        Map<String, List<ShopExcel>> shopExcelMap= shopExcels.stream()
                .collect(Collectors.groupingBy(ShopExcel::getItem_id));

        //  spu数据处理
        return convert(shopExcelMap);

    }

    private Map<String, Shop> convert(Map<String, List<ShopExcel>> shopExcelMap) {
        HashMap<String, Shop> hashMap = new HashMap<>();
        for (Map.Entry<String, List<ShopExcel>> entry : shopExcelMap.entrySet()) {
            String key = entry.getKey();
            List<ShopExcel> value = entry.getValue();
            ArrayList<Shop.Sku> skus = new ArrayList<>();
            ArrayList<Map> cats = new ArrayList<>();
            ArrayList<Shop.Attr> attrsSpu = new ArrayList<>(new HashSet<>());
            for(ShopExcel e : value){
                ArrayList<Shop.Attr> attrsSku = new ArrayList<>();
                if(StringUtils.isNotBlank(e.getAttr_name_1())){
                    attrsSku.add(new Shop.Attr(e.getAttr_name_1(), e.getAttr_value_1()));
                }
                if(StringUtils.isNotBlank(e.getAttr_name_2())){
                    attrsSku.add(new Shop.Attr(e.getAttr_name_2(), e.getAttr_value_2()));
                }
                if(StringUtils.isNotBlank(e.getAttr_name_3())){
                    attrsSku.add(new Shop.Attr(e.getAttr_name_3(), e.getAttr_value_3()));
                }
                Shop.Sku sku = new Shop.Sku();
                sku.setSku_id(e.getSku_id());
                sku.setSale_price(Convert.toLong(e.getSale_price()));
                sku.setStock_num(Convert.toLong(e.getStock_num()));
                sku.setSku_attrs(attrsSku);
                skus.add(sku);
                attrsSpu.addAll(attrsSku);
            }
            // 去重
            attrsSpu = new ArrayList<>(new HashSet<>(attrsSpu));
            if(StringUtils.isNotBlank(value.get(0).getLevel_1_cat_name())){
                HashMap<Object, Object> map = new HashMap<>();
                map.put("level_1_cat_name", value.get(0).getLevel_1_cat_name());
                cats.add(map);
            }
            if(StringUtils.isNotBlank(value.get(0).getLevel_2_cat_name())){
                HashMap<Object, Object> map = new HashMap<>();
                map.put("level_2_cat_name", value.get(0).getLevel_2_cat_name());
                cats.add(map);
            }
            if(StringUtils.isNotBlank(value.get(0).getLevel_3_cat_name())){
                HashMap<Object, Object> map = new HashMap<>();
                map.put("level_3_cat_name", value.get(0).getLevel_3_cat_name());
                cats.add(map);
            }
            Date date = DateUtils.parseDate(value.get(0).getEdit_time());
            hashMap.put(key, Shop.builder()
                    .attrs(attrsSpu)
                    .cats(cats)
                    .skus(skus)
                    .brand(value.get(0).getBrand())
                    .item_id(key)
                    .edit_time(date==null?null:date.getTime())
                    .status(value.get(0).getStatus())
                    .title(value.get(0).getTitle())
                    .build()) ;


        }
        return hashMap;

    }


    public static void main(String[] args){
        File file = new File("D:\\1Anets.top\\code\\CloudFriendDemo\\src\\main\\resources\\商品数据.xlsx");
        try (InputStream in =new FileInputStream(file)){
            List<ShopExcel> shopExcels = ShopDao.readExcel(in ,null);
            System.out.println(shopExcels.size());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 读取表格
     * @param inputStream
     * @param clazz
     * @param <T>
     * @return
     */
    private static   List<ShopExcel> readExcel(InputStream inputStream,List<String> ids) {
        List<ShopExcel> list = Collections.synchronizedList(new ArrayList<ShopExcel>());
        AtomicInteger total = new AtomicInteger(0);
        EasyExcel.read(inputStream, ShopExcel.class, new AnalysisEventListener<ShopExcel>() {
            @Override
            public void invoke(ShopExcel data, AnalysisContext context) {
                if(ids == null || ids.contains(data.getItem_id())){
                    total.incrementAndGet();
                    list.add(data);
                }
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
                System.out.println("共匹配"+total.get()+"条");
            }
        }).sheet().doRead();
        return list;
    }
}
