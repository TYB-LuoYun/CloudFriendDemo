package top.anets.module.shop.model;

/**
 * @author ftm
 * @date 2024/5/29 0029 23:07
 */

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ShopExcel {
//    @ExcelIgnore

    /**
     * 指定value，防止字段顺序改变而取错
     */
    @ExcelProperty(value = "item_id")
    String item_id;

    @ExcelProperty(value = "title")
    String title;

    @ExcelProperty(value = "status")
    String status;

    @ExcelProperty(value = "brand")
    String brand;

    @ExcelProperty(value = "level_1_cat_name")
    String level_1_cat_name;

    @ExcelProperty(value = "level_2_cat_name")
    String level_2_cat_name;

    @ExcelProperty(value = "level_3_cat_name")
    String level_3_cat_name;

    @ExcelProperty(value = "sku_id")
    String sku_id;

    @ExcelProperty(value = "attr_name_1")
    String attr_name_1;

    @ExcelProperty(value = "attr_value_1")
    String attr_value_1;

    @ExcelProperty(value = "attr_name_2")
    String attr_name_2;

    @ExcelProperty(value = "attr_value_2")
    String attr_value_2;

    @ExcelProperty(value = "attr_name_3")
    String attr_name_3;

    @ExcelProperty(value = "attr_value_3")
    String attr_value_3;

    @ExcelProperty(value = "sale_price")
    String sale_price;

    @ExcelProperty(value = "stock_num")
    String stock_num;

    @ExcelProperty(value = "edit_time")
    String edit_time;




}
