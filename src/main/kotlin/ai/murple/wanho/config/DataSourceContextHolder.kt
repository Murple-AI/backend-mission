package ai.murple.wanho.config

object DataSourceContextHolder {
    private val contextHolder: ThreadLocal<String> = ThreadLocal()

    fun setDataSourceType(type: String) {
        contextHolder.set(type)
    }

    fun getDataSourceType(): String? {
        return contextHolder.get()
    }

    fun clear() {
        contextHolder.remove()
    }

}