import { mount } from '@vue/test-utils';
import ViewerOverviewComponent from '../ViewerOverviewComponent.vue';

let wrapper;

// Define constants for element selectors
const REPORTS_CONTAINER = '.reportsContainerWrapper';
const SEND_REPORT_BUTTON = '.sendReportButton'
const DELETE_REPORT_BUTTON = '.deleteReport';
const SELECT_RECEIVER = '.reportReceiverSelect';
const REPORT_TEXT_FIELD = '.reportInput';

beforeEach(() => {
    wrapper = mount(ViewerOverviewComponent);
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

    it('displays the username, user team, and user reports', () => {

        const welcomeText = wrapper.find('.hiText');
        const teamInfo = wrapper.findAll('.infoValue').at(0);
        const projectInfo = wrapper.findAll('.infoValue').at(1);
        const reportsInfo = wrapper.findAll('.infoValue').at(2);

        const welcomeTextContent = welcomeText.text();
        const teamInfoContent = teamInfo.text();
        const projectInfoContent = projectInfo.text();
        const reportsInfoContent = reportsInfo.text();

        expect(welcomeTextContent).toContain(wrapper.vm.username);
        const expectedTeamName = wrapper.vm.currentTeam?.name ?? "Currently not in a team";
        expect(teamInfoContent).toContain(expectedTeamName);
        expect(parseInt(projectInfoContent)).toEqual(wrapper.vm.userProjects.length);
        expect(parseInt(reportsInfoContent)).toEqual(wrapper.vm.reports.length);
    });

    it('calls modal on delete reports', async () => {
        const showModalSpy = jest.spyOn(wrapper.vm, 'showModal');
        await wrapper.vm.showModal();
        expect(showModalSpy).toHaveBeenCalled();
    });

    it('has a send report container', () => {
        const selectUserInput = wrapper.find(SELECT_RECEIVER);
        expect(selectUserInput.exists()).toBe(true);

        const reportInput = wrapper.find(REPORT_TEXT_FIELD);
        expect(reportInput.exists()).toBe(true);

        const sendReportButton = wrapper.find(SEND_REPORT_BUTTON);
        expect(sendReportButton.exists()).toBe(true);
    });

});