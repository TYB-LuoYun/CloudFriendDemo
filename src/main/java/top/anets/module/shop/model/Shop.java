package top.anets.module.shop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author ftm
 * @date 2024/5/29 0029 23:07
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Shop implements Serializable {
    String item_id;
    String title;
    String brand;
    List<Map> cats;
    List<Attr> attrs;
    String status;
    List<Sku> skus;
    Long min_price;
    Long edit_time;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Attr implements Serializable{
       String attr_key;
       String attr_value;


        // 重写 equals() 和 hashCode()
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Attr attr = (Attr) o;
            return Objects.equals(attr_key, attr.attr_key) && Objects.equals(attr_value, attr.attr_value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(attr_key, attr_value);
        }

    }

    @Data
    public static class Sku implements Serializable{
        String sku_id;
        Long sale_price;
        Long stock_num;
        String sku_code;
        List<Attr> sku_attrs;
    }
}
