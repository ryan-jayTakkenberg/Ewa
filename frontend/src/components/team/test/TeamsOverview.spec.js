import { mount } from '@vue/test-utils';
import TeamsOverview from '@/components/team/TeamsOverview';

let wrapper;

const SEARCHBAR_PLACEHOLDER_CONTAINS = 'Search For Teams';
const TABLE_HEADER_CONTAINS = 'team';
const CREATE_BTN = 'Create Team';
// FAST: Focused, Automated, Small, and Testable
describe('TeamsOverview', () => {
    beforeEach(() => {
        // Automated: Setting up the component using mount from @vue/test-utils
        wrapper = mount(TeamsOverview, {
            // Focused: Providing necessary dependencies for the component
            global: {
                provide: {
                    teamsService: {
                        // Testable: Mocking asynchronous functions for testing
                        asyncFindAllWithProjectCount: jest.fn(() => Promise.resolve([])),
                        asyncDeleteById: jest.fn(() => Promise.resolve()),
                        asyncUpdateTeam: jest.fn(() => Promise.resolve()),
                        asyncSaveTeam: jest.fn(() => Promise.resolve())
                    },
                    warehouseService: {
                        asyncFindAll: jest.fn(() => Promise.resolve([]))
                    }
                }
            }
        });
    });

    // AAA: Arrange, Act, and Assert
    it('creates a proper page structure', () => {
        // Arrange: Wrapper is already created in beforeEach
        // Act: Accessing the wrapper's element and checking the number of children
        // Assert: Expecting the number of children to be greater than 0
        expect(wrapper.element.children.length).toBeGreaterThan(0);
    });

    it('has searchbar', () => {
        // Arrange: Wrapper is already created in beforeEach
        // Act: Finding the component named 'SolarSearchbar'
        const searchbar = wrapper.findComponent({ name: 'SolarSearchbar' });
        // Assert: Checking if the searchbar exists and has the correct placeholder
        expect(searchbar.exists()).toBe(true);
        expect(searchbar.props('placeHolder')).toBe(SEARCHBAR_PLACEHOLDER_CONTAINS);
    });

    it('has create button', () => {
        // Arrange: Wrapper is already created in beforeEach
        // Act: Finding the component named 'SolarButton'
        const button = wrapper.findComponent({ name: 'SolarButton' });
        // Assert: Checking if the button exists and has the correct text
        expect(button.exists()).toBe(true);
        expect(button.text().toLowerCase()).toContain(CREATE_BTN.toLowerCase());
    });

    it('has table', () => {
        // Arrange: Wrapper is already created in beforeEach
        // Act: Finding all 'th' elements and checking if any contains the TABLE_HEADER_CONTAINS text
        const teamTableHeader = wrapper.findAll('th').find(th => th.element.innerHTML?.toLowerCase().includes(TABLE_HEADER_CONTAINS.toLowerCase()));
        // Assert: Checking if the team table header exists
        expect(teamTableHeader?.exists()).toBe(true);
    });
});
