package org.odk.collect.android.widgets.items

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.javarosa.core.model.FormIndex
import org.javarosa.form.api.FormEntryPrompt
import org.odk.collect.android.databinding.SelectOneFromMapDialogLayoutBinding
import org.odk.collect.android.formentry.FormEntryViewModel
import org.odk.collect.android.injection.DaggerUtils
import org.odk.collect.androidshared.livedata.MutableNonNullLiveData
import org.odk.collect.androidshared.livedata.NonNullLiveData
import org.odk.collect.androidshared.ui.FragmentFactoryBuilder
import org.odk.collect.geo.MappableSelectItem
import org.odk.collect.geo.SelectionMapData
import org.odk.collect.geo.SelectionMapFragment
import org.odk.collect.material.MaterialFullScreenDialogFragment
import javax.inject.Inject

class SelectOneFromMapDialogFragment : MaterialFullScreenDialogFragment() {

    @Inject
    lateinit var formEntryViewModelFactory: FormEntryViewModel.Factory
    private val formEntryViewModel: FormEntryViewModel by activityViewModels { formEntryViewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerUtils.getComponent(context).inject(this)

        childFragmentManager.fragmentFactory = FragmentFactoryBuilder()
            .forClass(SelectionMapFragment::class.java) {
                val formIndex = requireArguments().getSerializable(ARG_FORM_INDEX) as FormIndex
                val prompt = formEntryViewModel.getQuestionPrompt(formIndex)
                SelectionMapFragment(SelectChoicesMapData(prompt))
            }
            .build()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = SelectOneFromMapDialogLayoutBinding.inflate(inflater)
        return binding.root
    }

    override fun getToolbar(): Toolbar? {
        return null
    }

    override fun onBackPressed() {
        dismiss()
    }

    override fun onCloseClicked() {
        // No toolbar so not relevant
    }

    companion object {
        const val ARG_FORM_INDEX = "form_index"
    }
}

private class SelectChoicesMapData(prompt: FormEntryPrompt) : SelectionMapData {

    private val mapTitle = MutableLiveData(prompt.longText)
    private val itemCount = MutableLiveData(prompt.selectChoices.size)

    override fun getMapTitle(): LiveData<String> {
        return mapTitle
    }

    override fun getItemType(): String {
        return "Choices"
    }

    override fun getItemCount(): LiveData<Int> {
        return itemCount
    }

    override fun getMappableItems(): NonNullLiveData<List<MappableSelectItem>> {
        return MutableNonNullLiveData(emptyList())
    }
}
