package com.fii.practic.mes.wip.general.search;

import com.fii.practic.mes.wip.general.AbstractEntity;
import com.fii.practic.mes.wip.general.AbstractRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import io.vertx.core.http.HttpServerResponse;

import java.util.Map;
import java.util.stream.Stream;

public final class HQLSearchExecutor {
    private HQLSearchExecutor() {
        // do nothing
    }

    public static <E extends AbstractEntity> Stream<E> executeSearch(final String queryString,
                                                                     final Map<String, Object> params,
                                                                     final HttpServerResponse response,
                                                                     final AbstractRepository<E> repository) {

       return executeSearch(queryString, params, Sort.descending("updated"), response, repository, Page.of(0, 1000));
    }

    public static <E extends AbstractEntity> Stream<E> executeSearch(final String queryString,
                                                              final Map<String, Object> params,
                                                              final Sort sorting,
                                                              final HttpServerResponse response,
                                                              final AbstractRepository<E> repository,
                                                              final Page page) {
        PanacheQuery<E> query = repository.find(queryString, sorting, params);

        long entitiesCount = query.count();
        long firstIndex = ((long) page.index * page.size) <= entitiesCount
                ? (long) page.index * page.size
                : 0L;
        long lastIndex = Math.min(firstIndex + page.size, entitiesCount);
        response.putHeader("Content-Range", "items %d-%d/%d".formatted(firstIndex, lastIndex, entitiesCount));

        query.page(page);
        return query.list().stream();
    }
}
