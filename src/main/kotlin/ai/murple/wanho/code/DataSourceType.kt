package ai.murple.wanho.code

/**
 * 데이터 소스 ENUM
 *
 * - `WRITE`: 쓰기 작업
 * - `READ`: 읽기 작업 (@ReadOnly 사용)
 */
enum class DataSourceType {
    WRITE,
    READ
}