import { mount } from '@vue/test-utils';
import OverviewModal from '@/components/overview/OverviewModal.vue';

let wrapper;
jest.mock('axios');

// Define constants for element selectors
const MODAL_DESCRIPTION = '.description';
const REPORT_CONTAINER = '.reportContainer';
const WARNING_TEXT = '.warningText';
const CANCEL_BUTTON = '.noButton';
const CONFIRM_BUTTON = '.yesButton';

beforeEach(() => {
    wrapper = mount(OverviewModal, {
        mocks: {
            $nextTick: jest.fn(),
        },
    });
});

afterEach(() => {
    jest.restoreAllMocks();
});

describe('OverviewModal', () => {

    it('renders properly', () => {
        const modalDescription = wrapper.find(MODAL_DESCRIPTION);
        const reportContainer = wrapper.find(REPORT_CONTAINER);
        const warningText = wrapper.find(WARNING_TEXT);
        const cancelButton = wrapper.find(CANCEL_BUTTON);
        const confirmButton = wrapper.find(CONFIRM_BUTTON);

        expect(wrapper.exists()).toBe(true);
        expect(modalDescription.exists()).toBe(true);
        expect(reportContainer.exists()).toBe(true);
        expect(warningText.exists()).toBe(true);
        expect(cancelButton.exists()).toBe(true);
        expect(confirmButton.exists()).toBe(true);
    });

    // FIRST
    it('cancel confirmation', () => {
        // Arrange: Find the cancel button in the wrapper
        const cancelButton = wrapper.find(CANCEL_BUTTON);

        // Act: Simulate a click on the cancel button
        cancelButton.trigger('click');

        // Assert: Verify that the cancelDelete method is called by checking emitted event
        expect(cancelButton.exists()).toBe(true);
        expect(wrapper.emitted('cancel-delete')).toBeTruthy();
    });

    // FIRST
    it('confirm confirmation', () => {
        // Arrange: Find the confirm button in the wrapper
        const confirmButton = wrapper.find(CONFIRM_BUTTON);

        // Act: Simulate a click on the confirm button
        confirmButton.trigger('click');

        // Assert: Verify that the confirmDelete method is called by checking emitted event
        expect(confirmButton.exists()).toBe(true);
        expect(wrapper.emitted('confirm-delete')).toBeTruthy();
    });

});