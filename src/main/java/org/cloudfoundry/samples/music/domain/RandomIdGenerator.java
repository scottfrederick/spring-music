package org.cloudfoundry.samples.music.domain;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.UUID;

public class RandomIdGenerator implements IdentifierGenerator {
    @Override
    public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
        return generateId();
    }

    public String generateId() {
        return UUID.randomUUID().toString();
    }
}
