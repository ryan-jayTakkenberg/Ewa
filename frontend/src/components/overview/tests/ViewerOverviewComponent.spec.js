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

    // FIRST
    it('displays the username, user team, and user reports', () => {

        // Arrange: Find elements in the wrapper for welcome text, team info, project info, and reports info
        const welcomeText = wrapper.find('.hiText');
        const teamInfo = wrapper.findAll('.infoValue').at(0);
        const projectInfo = wrapper.findAll('.infoValue').at(1);
        const reportsInfo = wrapper.findAll('.infoValue').at(2);

        // Act: Retrieve the text content of the elements
        const welcomeTextContent = welcomeText.text();
        const teamInfoContent = teamInfo.text();
        const projectInfoContent = projectInfo.text();
        const reportsInfoContent = reportsInfo.text();

        // Assert: Validate the displayed information against the expected values
        expect(welcomeTextContent).toContain(wrapper.vm.username);
        const expectedTeamName = wrapper.vm.currentTeam?.name ?? "Currently not in a team";
        expect(teamInfoContent).toContain(expectedTeamName);
        expect(parseInt(projectInfoContent)).toEqual(wrapper.vm.userProjects.length);
        expect(parseInt(reportsInfoContent)).toEqual(wrapper.vm.reports.length);
    });

    // FIRST
    it('calls modal on delete reports', async () => {
        // Arrange: Spy on the showModal method of the component's view model
        const showModalSpy = jest.spyOn(wrapper.vm, 'showModal');

        // Act: Call the showModal method asynchronously
        await wrapper.vm.showModal();

        // Assert: Verify that the showModal method has been called
        expect(showModalSpy).toHaveBeenCalled();
    });

    // FIRST
    it('has a send report container', () => {
        // Arrange: Find elements in the wrapper for select user input, report input, and send report button
        const selectUserInput = wrapper.find(SELECT_RECEIVER);
        const reportInput = wrapper.find(REPORT_TEXT_FIELD);
        const sendReportButton = wrapper.find(SEND_REPORT_BUTTON);

        // Assert: Verify the existence of each component in the send report container
        expect(selectUserInput.exists()).toBe(true);
        expect(reportInput.exists()).toBe(true);
        expect(sendReportButton.exists()).toBe(true);
    });


});