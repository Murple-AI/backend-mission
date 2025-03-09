package ai.murple.wanho.repository

import ai.murple.wanho.entity.KoreanLastNameEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface KoreanLastNameRepository: JpaRepository<KoreanLastNameEntity, Long>