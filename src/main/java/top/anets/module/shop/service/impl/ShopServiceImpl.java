package top.anets.module.shop.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.collect.Iterables;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import top.anets.exception.ServiceException;
import top.anets.module.shop.cache.ShopCache;
import top.anets.module.shop.model.ShopExcel;
import top.anets.module.shop.service.ShopService;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * @author ftm
 * @date 2024/5/29 0029 22:58
 */
@Service
public class ShopServiceImpl implements ShopService {
    @Autowired
    ShopCache shopCache;
    @Override
    public List list(List<String> ids) {
        return shopCache.getAll(ids);
    }









}
