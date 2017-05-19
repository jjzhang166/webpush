package com.github.wuzguo.server.server;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 会话uuid缓存
 *
 * @author wuzguo
 * @date 2016年12月16日 下午2:24:24
 */
public class SessionUUIDCached {

    private static final Map<String, Set<UUID>> SESSIONS = new ConcurrentHashMap();

    /**
     * 添加uuid
     *
     * @param key
     * @param uuid
     * @return
     */
    public static boolean add(final String key, final UUID uuid) {
        Set<UUID> uuids = SESSIONS.get(key);
        // 如果为空
        if (uuids == null) {
            uuids = new HashSet(5);
            SESSIONS.put(key, uuids);
        }
        return SESSIONS.get(key).add(uuid);
    }

    /**
     * 获取uuid
     *
     * @param key
     * @return
     */
    public static Set<UUID> get(final String key) {
        return SESSIONS.get(key);
    }

    /**
     * 移除uuid
     *
     * @param key
     * @param uuid
     * @return
     */
    public static Set<UUID> remove(final String key, final UUID uuid) {
        Set<UUID> uuids = SESSIONS.get(key);
        if (uuids == null) {
            return null;
        }
        if (uuids.contains(uuid)) {
            uuids.remove(uuid);
        }
        if (uuids.isEmpty()) {
            SESSIONS.remove(key);
        }
        return uuids;
    }

    /**
     * 获取session缓存集合
     *
     * @return
     */
    public static Map<String, Set<UUID>> getSessions() {
        return SESSIONS;
    }
}
