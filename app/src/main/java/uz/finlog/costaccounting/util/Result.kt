package uz.finlog.costaccounting.util

sealed class Result<out T> {
    data class Success<out T>(val value: T) : Result<T>()
    data class Failure(val exception: Throwable) : Result<Nothing>()

    // Функция для извлечения значения в случае успеха
    fun getOrNull(): T? = (this as? Success)?.value

    // Функция для получения исключения в случае неудачи
    fun exceptionOrNull(): Throwable? = (this as? Failure)?.exception
}