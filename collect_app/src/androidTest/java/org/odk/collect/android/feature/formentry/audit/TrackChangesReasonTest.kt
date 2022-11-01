package org.odk.collect.android.feature.formentry.audit

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.runner.RunWith
import org.odk.collect.android.R
import org.odk.collect.android.support.StorageUtils
import org.odk.collect.android.support.pages.ChangesReasonPromptPage
import org.odk.collect.android.support.pages.FormEntryPage
import org.odk.collect.android.support.pages.MainMenuPage
import org.odk.collect.android.support.pages.SaveOrIgnoreDialog
import org.odk.collect.android.support.rules.CollectTestRule
import org.odk.collect.android.support.rules.TestRuleChain

@RunWith(AndroidJUnit4::class)
class TrackChangesReasonTest {

    var rule = CollectTestRule()

    @get:Rule
    var copyFormChain: RuleChain = TestRuleChain.chain()
        .around(rule)

    @Test
    fun openingAFormToEdit_andChangingAValue_andClickingSaveAndExit_andEnteringReason_andClickingSave_returnsToMainMenu() {
        rule.startAtMainMenu()
            .copyForm("track-changes-reason-on-edit.xml")
            .startBlankForm("Track Changes Reason")
            .inputText("Nothing much...")
            .swipeToEndScreen()
            .clickSaveAndExit()
            .clickEditSavedForm()
            .clickOnForm("Track Changes Reason")
            .clickGoToStart()
            .inputText("Nothing much!")
            .swipeToEndScreen()
            .clickSaveAndExitWithChangesReasonPrompt()
            .enterReason("Needed to be more exciting and less mysterious")
            .clickSave()

        val auditLogForFirstInstance = StorageUtils.getAuditLogForFirstInstance()
        assertThat(auditLogForFirstInstance[10].get("event"), equalTo("change reason"))
        assertThat(
            auditLogForFirstInstance[10].get("change-reason"),
            equalTo("Needed to be more exciting and less mysterious")
        )
    }

    @Test
    fun openingAFormToEdit_andChangingAValue_andClickingSaveAndExit_andPressingBack_returnsToForm() {
        rule.startAtMainMenu()
            .copyForm("track-changes-reason-on-edit.xml")
            .startBlankForm("Track Changes Reason")
            .inputText("Nothing much...")
            .swipeToEndScreen()
            .clickSaveAndExit()
            .clickEditSavedForm()
            .clickOnForm("Track Changes Reason")
            .clickGoToStart()
            .inputText("Nothing much!")
            .swipeToEndScreen()
            .clickSaveAndExitWithChangesReasonPrompt()
            .closeSoftKeyboard()
            .pressBack(FormEntryPage("Track Changes Reason"))
            .assertText(R.string.save_form_as)
    }

    @Test
    fun openingAFormToEdit_andChangingAValue_andClickingSaveAndExit_andClickingCross_returnsToForm() {
        rule.startAtMainMenu()
            .copyForm("track-changes-reason-on-edit.xml")
            .startBlankForm("Track Changes Reason")
            .inputText("Nothing much...")
            .swipeToEndScreen()
            .clickSaveAndExit()
            .clickEditSavedForm()
            .clickOnForm("Track Changes Reason")
            .clickGoToStart()
            .inputText("Nothing much!")
            .swipeToEndScreen()
            .clickSaveAndExitWithChangesReasonPrompt()
            .closeSoftKeyboard()
            .pressClose(FormEntryPage("Track Changes Reason"))
            .assertText(R.string.save_form_as)
    }

    @Test
    fun openingAFormToEdit_andChangingAValue_andClickingSaveAndExit_andRotating_remainsOnPrompt() {
        rule.startAtMainMenu()
            .copyForm("track-changes-reason-on-edit.xml")
            .startBlankForm("Track Changes Reason")
            .inputText("Nothing much...")
            .swipeToEndScreen()
            .clickSaveAndExit()
            .clickEditSavedForm()
            .clickOnForm("Track Changes Reason")
            .clickGoToStart()
            .inputText("Nothing much!")
            .swipeToEndScreen()
            .clickSaveAndExitWithChangesReasonPrompt()
            .enterReason("Something")
            .rotateToLandscape(ChangesReasonPromptPage("Track Changes Reason"))
            .assertText("Something")
            .closeSoftKeyboard()
            .clickSave()
    }

    @Test
    fun openingAFormToEdit_andChangingAValue_andPressingBack_andClickingSaveChanges_promptsForReason() {
        rule.startAtMainMenu()
            .copyForm("track-changes-reason-on-edit.xml")
            .startBlankForm("Track Changes Reason")
            .inputText("Nothing much...")
            .swipeToEndScreen()
            .clickSaveAndExit()
            .clickEditSavedForm()
            .clickOnForm("Track Changes Reason")
            .clickGoToStart()
            .inputText("Nothing much!")
            .closeSoftKeyboard()
            .pressBack(SaveOrIgnoreDialog("Track Changes Reason",
                ChangesReasonPromptPage("Track Changes Reason")))
            .clickSaveChanges()
    }

    @Test
    fun openingAFormToEdit_andChangingAValue_andPressingBack_andIgnoringChanges_returnsToMainMenu() {
        rule.startAtMainMenu()
            .copyForm("track-changes-reason-on-edit.xml")
            .startBlankForm("Track Changes Reason")
            .inputText("Nothing much...")
            .swipeToEndScreen()
            .clickSaveAndExit()
            .clickEditSavedForm()
            .clickOnForm("Track Changes Reason")
            .clickGoToStart()
            .inputText("Nothing much!")
            .closeSoftKeyboard()
            .pressBack(SaveOrIgnoreDialog("Track Changes Reason", MainMenuPage()))
            .clickIgnoreChanges()
    }

    @Test
    fun openingAFormToEdit_andNotChangingAValue_andClickingSaveAndExit_returnsToMainMenu() {
        rule.startAtMainMenu()
            .copyForm("track-changes-reason-on-edit.xml")
            .startBlankForm("Track Changes Reason")
            .inputText("Nothing much...")
            .swipeToEndScreen()
            .clickSaveAndExit()
            .clickEditSavedForm()
            .clickOnForm("Track Changes Reason")
            .clickGoToStart()
            .closeSoftKeyboard()
            .swipeToEndScreen()
            .clickSaveAndExit()
    }

    @Test
    fun openingFormToEdit_andChangingValue_andClickingSave_promptsForReason() {
        rule.startAtMainMenu()
            .copyForm("track-changes-reason-on-edit.xml")
            .startBlankForm("Track Changes Reason")
            .inputText("Nothing much...")
            .swipeToEndScreen()
            .clickSaveAndExit()
            .clickEditSavedForm()
            .clickOnForm("Track Changes Reason")
            .clickGoToStart()
            .inputText("Nothing much!")
            .clickSaveWithChangesReasonPrompt()
            .enterReason("Bah")
            .clickSave(FormEntryPage("Track Changes Reason"))
            .assertQuestion("What up?")
    }
}
