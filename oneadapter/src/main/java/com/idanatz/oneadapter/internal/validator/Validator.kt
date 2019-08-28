package com.idanatz.oneadapter.internal.validator

import android.content.Context
import com.idanatz.oneadapter.external.interfaces.Diffable
import com.idanatz.oneadapter.internal.ViewHolderCreatorsStore
import com.idanatz.oneadapter.internal.holders.InternalHolderModel
import java.lang.NullPointerException

internal class Validator {

    companion object {

        fun validateItemsAgainstRegisteredModules(viewHolderCreatorsStore: ViewHolderCreatorsStore, items: List<Diffable>) {
            items.filterNot { it is InternalHolderModel }.find { viewHolderCreatorsStore.getCreator(it.javaClass) == null }?.let {
                throw MissingModuleDefinitionException("did you forget to attach ItemModule? (model: ${it.javaClass})")
            }
        }

        fun validateLayoutExists(context: Context?, layoutId: Int) {
            try {
                context?.resources?.getResourceEntryName(layoutId) ?: throw NullPointerException()
            } catch (e: Exception) {
                throw MissingConfigArgumentException("Layout resource id not found")
            }
        }

        fun validateItemModuleAgainstRegisteredModules(viewHolderCreatorsStore: ViewHolderCreatorsStore, dataClass: Class<*>) {
            if (viewHolderCreatorsStore.isCreatorExists(dataClass)) {
                throw MultipleHolderConflictException("ItemModule with model class ${dataClass.simpleName} already attached")
            }
        }
    }
}