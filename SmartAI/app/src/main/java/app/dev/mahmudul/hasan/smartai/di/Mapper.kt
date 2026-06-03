package app.dev.mahmudul.hasan.smartai.di

interface Mapper<F,T> {
    fun map(from: F): T
}