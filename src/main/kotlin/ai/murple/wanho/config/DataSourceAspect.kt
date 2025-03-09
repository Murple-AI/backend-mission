package ai.murple.wanho.config

import ai.murple.wanho.code.DataSourceType
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.After
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.stereotype.Component

@Aspect
@Component
class DataSourceAspect {

    @After("@annotation(ReadOnly)")
    fun setReadDataSource() {
        DataSourceContextHolder.setDataSourceType(DataSourceType.READ.name)
    }

    @After("@annotation(ReadOnly)")
    fun clearDataSource() {
        DataSourceContextHolder.clear()
    }
}
