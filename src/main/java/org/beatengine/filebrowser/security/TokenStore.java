package org.beatengine.filebrowser.security;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TokenStore {

    private static TokenStore store;

    public static int CLEAR_INVALID_TOKENS_EVERY_SEC = 300;

    private final Map<String, Token> tokens = new HashMap<>();

    private Instant lastClean = Instant.now();

    public static synchronized TokenStore getStore()
    {
        if(store == null)
        {
            store = new TokenStore();
        }
        return store;
    }

    private synchronized void clearExpiredTokens()
    {
        lastClean = Instant.now();
        final List<String> removeKeys = new ArrayList<>();
        for(Map.Entry<String, Token> entry : tokens.entrySet())
        {
            if(entry.getValue().isExpired())
            {
                removeKeys.add(entry.getKey());
            }
        }
        for (final String key: removeKeys)
        {
            tokens.remove(key);
        }
    }

    private void clearEveryPeriod()
    {
        if(lastClean.isBefore(Instant.now().minusSeconds(CLEAR_INVALID_TOKENS_EVERY_SEC)))
        {
            clearExpiredTokens();
        }
    }

    private synchronized void insertToken(final String token, final Token t)
    {
        tokens.put(token, t);
    }

    /**
     * @param token token-hash
     * @return The Token, which also holds the <strong>userId</strong>
     */
    public Token findByToken(final String token)
    {
        return tokens.get(token);
    }

    public void setToken(final String token, final Token t)
    {
        insertToken(token, t);
        clearEveryPeriod();
    }

    public synchronized void removeToken(final String token)
    {
        tokens.remove(token);
    }

}
