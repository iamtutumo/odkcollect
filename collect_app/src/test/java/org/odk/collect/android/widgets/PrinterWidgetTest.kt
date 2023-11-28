package org.odk.collect.android.widgets

import com.google.android.material.button.MaterialButton
import org.javarosa.core.model.data.IAnswerData
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever
import org.odk.collect.android.R
import org.odk.collect.android.formentry.questions.QuestionDetails
import org.odk.collect.android.utilities.QuestionMediaManager
import org.odk.collect.android.widgets.base.QuestionWidgetTest
import org.odk.collect.android.widgets.utilities.PrintableHtmlParser
import org.odk.collect.printer.HtmlPrinter

class PrinterWidgetTest : QuestionWidgetTest<PrinterWidget, IAnswerData>() {
    private val questionMediaManager = mock<QuestionMediaManager>()
    private val printableHtmlParser = mock<PrintableHtmlParser>()
    private val htmlPrinter = mock<HtmlPrinter>()

    override fun createWidget() = PrinterWidget(activity, QuestionDetails(formEntryPrompt), questionMediaManager, printableHtmlParser, htmlPrinter)

    @Test
    fun `clicking the button should trigger printing parsed html document if answer exists`() {
        whenever(formEntryPrompt.answerText).thenReturn("blah")
        whenever(printableHtmlParser.parse("blah", questionMediaManager)).thenReturn("test content")

        val widget = createWidget()
        widget.findViewById<MaterialButton>(R.id.printer_button).performClick()

        verify(htmlPrinter).print(activity, "test content")
    }

    @Test
    fun `clicking the button should not trigger printing if there is no answer`() {
        whenever(formEntryPrompt.answerText).thenReturn(null)

        val widget = createWidget()
        widget.findViewById<MaterialButton>(R.id.printer_button).performClick()

        verifyNoInteractions(htmlPrinter)
    }

    @Test
    override fun usingReadOnlyOptionShouldMakeAllClickableElementsDisabled() = Unit // ignore

    @Test
    override fun callingClearShouldRemoveTheExistingAnswer() = Unit // ignore

    @Test
    override fun callingClearShouldCallValueChangeListeners() = Unit // ignore

    @Test
    override fun getAnswerShouldReturnExistingAnswerIfPromptHasExistingAnswer() = Unit // ignore

    @Test
    override fun whenReadOnlyQuestionHasAnswer_answerContainerShouldBeDisplayed() = Unit // ignore

    override fun getNextAnswer() = null
}
