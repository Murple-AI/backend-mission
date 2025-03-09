package ai.murple.wanho.config

import ai.murple.wanho.code.DataSourceType
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource

class RoutingDataSource : AbstractRoutingDataSource() {
    override fun determineCurrentLookupKey(): Any {
        return DataSourceContextHolder.getDataSourceType() ?: DataSourceType.WRITE
    }
}