package top.anets.module.shop.cache;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.google.common.collect.Iterables;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.anets.module.shop.dao.ShopDao;
import top.anets.module.shop.model.Shop;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author ftm
 * @date 2024/5/30 0030 0:25
 */
@Component
public class ShopCache {

    @Resource
    private ShopDao shopDao;

    private LoadingCache<String,Shop> cache;
    
    @PostConstruct
    public void init(){
        cache = Caffeine.newBuilder()
                //自动刷新,不会阻塞线程,其他线程返回旧值
//                .refreshAfterWrite(60, TimeUnit.SECONDS)
                .expireAfterWrite(10*60, TimeUnit.SECONDS)
                .maximumSize(1024)
                .build(new CacheLoader<String, Shop>() {
                    @Nullable
                    @Override
                    public  Shop load(@NonNull String in) throws Exception {
                        return  ShopCache.this.load(Collections.singletonList(in)).get(in);
                    }

                    @Override
                    public @NonNull Map<String, Shop> loadAll(@NonNull Iterable<? extends String> keys) throws Exception {
                        String[] ins = Iterables.toArray(keys, String.class);
                        return ShopCache.this.load(Arrays.asList(ins));
                    }
                });
    }

    private Map<String, Shop> load(List<String>  ids) {
        return shopDao.listByIds(ids);
    }

    public List getAll(List<String> ids) {
       return new ArrayList<>(cache.getAll(ids).values());
    }
}
