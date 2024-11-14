package com.cristiangoncas.greenhousemonitor.domain.entity

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

object TimestampSerializer : KSerializer<Long> {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).apply {
        timeZone = TimeZone.getDefault()
    }

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("TimestampSerializer", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Long) {
        encoder.encodeString(dateFormat.format(value))
    }

    override fun deserialize(decoder: Decoder): Long {
        val dateString = decoder.decodeString()
        val date = dateFormat.parse(dateString) ?: throw IllegalArgumentException("Invalid date format")
        return date.time
    }
}