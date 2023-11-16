package com.hrms.search.query;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;

import java.util.Arrays;

public class HRMSQuery extends NativeQuery {
    public static NativeQuery getFuzzyQuery(String searchText, String fuzzyness, String... fields) {
        return NativeQuery.builder().withQuery(
                q -> q.multiMatch(
                        m -> m.fields(Arrays.asList(fields)).query(searchText).fuzziness(fuzzyness)
                )
        ).build();
    }

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
