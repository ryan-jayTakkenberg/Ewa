import { mount } from '@vue/test-utils';
import TeamsOverview from '@/components/team/TeamsOverview';

let wrapper;

const SEARCHBAR_PLACEHOLDER_CONTAINS = 'Search For Teams';
const TABLE_HEADER_CONTAINS = 'team';
const CREATE_BTN = 'Create Team';

describe('TeamsOverview', () => {
    beforeEach(() => {
        wrapper = mount(TeamsOverview, {
            global: {
                provide: {
                    teamsService: {
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

    it('creates a proper page structure', () => {
        expect(wrapper.element.children.length,
            `main page starting with ${wrapper.element.tagName} has no child elements`)
            .toBeGreaterThan(0);
    });

    it('has searchbar', () => {
        const searchbar = wrapper.findComponent({ name: 'SolarSearchbar' });
        expect(searchbar.exists(), `TeamsOverview is missing a searchbar`).toBe(true);
        expect(searchbar.props('placeHolder'), `Searchbar placeholder is incorrect`).toBe(SEARCHBAR_PLACEHOLDER_CONTAINS);
    });

    it('has create button', () => {
        const button = wrapper.findComponent({ name: 'SolarButton' });
        expect(button.exists(), `missing create team button`).toBe(true);
        expect(button.text().toLowerCase(), `Create team button text is incorrect`).toContain(CREATE_BTN.toLowerCase());
    });

    it('has table', () => {
        const teamTableHeader = wrapper.findAll('th')
            .find(th => th.element.innerHTML?.toLowerCase().includes(TABLE_HEADER_CONTAINS.toLowerCase()));
        expect(teamTableHeader?.exists(), `missing a team table`).toBe(true);
    });
});
