package com.hrms.search.query;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;

public class HRMSQuery extends NativeQuery {
    public static NativeQuery getFuzzyQuery(String searchText) {
        TextQueryType type = TextQueryType.MostFields;
        return NativeQuery.builder().withQuery(
                q -> q.multiMatch(
                        m -> m.type(type).query(searchText).fuzziness("AUTO")
                )
        ).build();
    }
    public HRMSQuery(NativeQueryBuilder builder) {
        super(builder);
    }

    public HRMSQuery(Query query) {
        super(query);
    }
}
