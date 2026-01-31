package com.childproduct.designassistant.model

data class DesignDocument(
    val id: String,
    val productName: String,
    val version: String,
    val createdDate: String,
    val sections: List<DocumentSection>,
    val images: List<String>
)

data class DocumentSection(
    val title: String,
    val content: String,
    val order: Int
)
