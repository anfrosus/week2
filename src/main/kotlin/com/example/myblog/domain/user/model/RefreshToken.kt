package com.example.myblog.domain.user.model

import com.example.myblog.domain.TimeStamped
import jakarta.persistence.*

@Entity
class RefreshToken(

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    var user:User,

    @Column(name = "TOKEN", nullable = false, columnDefinition = "TEXT")
    var tokenValue:String,

    @Column(name = "IS_USED", nullable = false)
    var isUsed:Boolean = false

) : TimeStamped() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}