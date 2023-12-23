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

    it('cancel confirmation', () => {
        const cancelButton = wrapper.find(CANCEL_BUTTON);

        expect(cancelButton.exists()).toBe(true);

        // Simulate click on cancel button
        cancelButton.trigger('click');

        // Verify that the cancelDelete method is called
        expect(wrapper.emitted('cancel-delete')).toBeTruthy();
    });

    it('confirm confirmation', () => {
        const confirmButton = wrapper.find(CONFIRM_BUTTON);

        expect(confirmButton.exists()).toBe(true);

        // Simulate click on confirm button
        confirmButton.trigger('click');

        // Verify that the confirmDelete method is called
        expect(wrapper.emitted('confirm-delete')).toBeTruthy();
    });

});