package com.arkhe.menu.utils

import android.util.Log
import com.arkhe.menu.data.remote.dto.ProfileResponseDto
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.request

object Logger {
    fun asLogSendingRequest(
        tag: String,
        requestJson: String,
        constants: Constants,
        response: HttpResponse
    ) {
        Log.d(tag, "========== API REQUEST START ==========")
        Log.d(tag, "🚀 SENDING REQUEST:")
        Log.d(tag, "   URL: ${constants.URL_BASE}")
        Log.d(tag, "   Action: ${constants.PARAMETER_VALUE_PROFILES}")
        Log.d(tag, "   Payload: $requestJson")
        Log.d(tag, "📤 REQUEST SENT:")
        Log.d(tag, "   Method: ${response.request.method.value}")
        Log.d(tag, "   URL: ${response.request.url}")
        Log.d(tag, "   Headers:")
        response.request.headers.entries().forEach { (key, values) ->
            values.forEach { value ->
                Log.d(tag, "      $key: $value")
            }
        }
    }

    fun asLogDetailsResponse(
        tag: String,
        response: HttpResponse,
        responseText: String
    ) {
        Log.d(tag, "📥 RESPONSE RECEIVED:")
        Log.d(tag, "   Status Code: ${response.status.value}")
        Log.d(tag, "   Status Description: ${response.status.description}")
        Log.d(tag, "   Response Headers:")
        response.headers.entries().forEach { (key, values) ->
            values.forEach { value ->
                Log.d(tag, "      $key: $value")
            }
        }
        Log.d(tag, "📄 RESPONSE BODY:")
        Log.d(tag, "   Body Length: ${responseText.length} characters")
        Log.d(tag, "   Raw Response: $responseText")
    }

    fun asLogJsonResponseSuccess(
        tag: String,
        parsedResponse: ProfileResponseDto,
    ) {
        Log.d(tag, "✅ JSON Response detected, parsing...SUCCESS:")
        Log.d(tag, "   Status: ${parsedResponse.status}")
        Log.d(tag, "   Message: ${parsedResponse.message}")
        Log.d(tag, "   Data Count: ${parsedResponse.data.size}")
        Log.d(tag, "========== API REQUEST SUCCESS ==========")
    }

    fun asLogJsonResponseException(
        tag: String,
        parseException: Exception,
    ) {
        Log.e(
            tag,
            "❌ JSON Parsing FAILED: ${parseException.message}",
            parseException
        )
        Log.e(tag, "========== API REQUEST PARSE ERROR ==========")
    }

    fun asLogNonJsonResponse(
        tag: String
    ) {
        Log.w(tag, "⚠️ Non-JSON Response received")
        Log.w(tag, "========== API REQUEST NON-JSON ==========")
    }

    fun asLogResponseHttpStatusCode303(
        tag: String,
        location: String?,
        response: HttpResponse,
        responseText: String
    ) {
        Log.e(tag, "❌ See Other (303)")
        Log.w(tag, "🔄 Redirect Response received")
        Log.w(tag, "   Redirect Location: $location")
        Log.w(tag, "   Original URL: ${response.request.url}")
        Log.w(tag, "   Response Body: $responseText")
        Log.w(tag, "========== API REQUEST REDIRECT ==========")

        if (location != null && location.isNotEmpty()) {
            Log.w(tag, "🔄 Following redirect manually...")
        }
    }

    fun asLogResponseHttpStatusCode405(
        tag: String,
        response: HttpResponse,
    ) {
        Log.e(tag, "❌ Method Not Allowed (405)")
        Log.e(tag, "   Server only allows: ${response.headers["Allow"]}")
        Log.e(tag, "   Current method: ${response.request.method.value}")
        Log.e(tag, "   Request URL: ${response.request.url}")
        Log.e(tag, "========== API REQUEST METHOD NOT ALLOWED ==========")
        Log.w(tag, "🔄 Retrying with alternative method...")
    }

    fun asLogResponseUnexpectedHttpStatusCode(
        tag: String,
        response: HttpResponse,
    ) {
        Log.e(tag, "❌ Unexpected Status Code: ${response.status}")
        Log.e(tag, "========== API REQUEST UNEXPECTED STATUS ==========")
    }

    fun asLogNetworkException(
        tag: String,
        e: Exception,
    ) {
        Log.e(tag, "💥 API CALL EXCEPTION: ${e.message}", e)
        Log.e(tag, "   Exception Type: ${e.javaClass.simpleName}")
        Log.e(tag, "   Stack Trace:")
        e.stackTrace.take(10).forEach { stackElement ->
            Log.e(tag, "      at $stackElement")
        }
        Log.e(tag, "========== API REQUEST FAILED ==========")
    }

    fun asLogResponseRetryWithAlternativeMethod(
        tag: String,
        responseText: String
    ) {
        Log.d(tag, "🔄 ALTERNATIVE METHOD: Using submitForm")
        Log.d(tag, "📥 ALTERNATIVE RESPONSE: $responseText")
    }

    fun asLogResponseRetryWithAlternativeMethodFailed(
        tag: String,
        e: Exception
    ) {
        Log.e(tag, "💥 ALTERNATIVE METHOD FAILED: ${e.message}", e)
    }

    fun asLogResponseFollowRedirectManually(
        tag: String,
        location: String?,
        response: HttpResponse,
        responseText: String
    ) {
        Log.d(tag, "🔄 MANUAL REDIRECT: Following to $location")
        Log.d(tag, "📥 REDIRECT RESPONSE: Status ${response.status.value}")
        Log.d(tag, "📥 REDIRECT BODY: $responseText")
    }

    fun asLogResponseFollowRedirectManuallyFailed(
        tag: String,
        e: Exception
    ) {
        Log.e(tag, "💥 MANUAL REDIRECT FAILED: ${e.message}", e)
    }
}
