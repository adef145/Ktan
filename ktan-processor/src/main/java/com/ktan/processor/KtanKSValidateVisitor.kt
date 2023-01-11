package com.ktan.processor

import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.symbol.KSTypeReference
import com.google.devtools.ksp.visitor.KSValidateVisitor

class KtanKSValidateVisitor : KSValidateVisitor({ _, _ -> true }) {

    override fun visitTypeReference(
        typeReference: KSTypeReference,
        data: KSNode?
    ): Boolean {
        return true
    }
}