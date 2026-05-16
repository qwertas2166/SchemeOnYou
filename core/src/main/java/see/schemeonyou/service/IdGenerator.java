package see.schemeonyou.service;

import see.di.See;

import java.util.UUID;

@See
public class IdGenerator {
    public String next(String prefix) {
        return prefix + "-" + UUID.randomUUID();
    }
}
