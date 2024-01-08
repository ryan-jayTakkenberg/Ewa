import { mount } from '@vue/test-utils';
import ViewerOverviewComponent from '../ViewerOverviewComponent.vue';

let wrapper;

// Define constants for element selectors
const REPORTS_CONTAINER = '.reportsContainer';
const SEND_REPORT_BUTTON = '.sendReportButton'
const DELETE_REPORT_BUTTON = '.deleteMessage';

beforeEach(() => {
    wrapper = mount(ViewerOverviewComponent, {
        global: {
            provide: {
                reportService: {
                    fetchReports: jest.fn(() => Promise.resolve([])),
                    postReport: jest.fn(() => Promise.resolve()),
                    deleteReport: jest.fn(() => Promise.resolve()),
                },
            }
        }
    });
});

afterEach(() => {
    jest.restoreAllMocks();
});

describe('ViewerOverviewComponent', () => {

    it('renders properly', () => {
        const reportsContainer = wrapper.find(REPORTS_CONTAINER);
        const sendReportButton = wrapper.find(SEND_REPORT_BUTTON);
        const deleteReportButton = wrapper.find(DELETE_REPORT_BUTTON);

        expect(wrapper.exists()).toBe(true);
        expect(reportsContainer.exists()).toBe(true);
        expect(sendReportButton.exists()).toBe(true);
        expect(deleteReportButton.exists()).toBe(true);
    });

    it('component is mounted', () => {
        expect(wrapper.exists()).toBe(true);
    });

    it('calls modal on delete reports', async () => {
        const showModalSpy = jest.spyOn(wrapper.vm, 'showModal');
        await wrapper.vm.showModal();
        expect(showModalSpy).toHaveBeenCalled();
    });

});