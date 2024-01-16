import { mount } from '@vue/test-utils';
import DeleteProductModal from './DeleteProductModal';
import Product from "@/models/product";

let wrapper;

const MODAL_TITLE_CONTAINS = 'Delete';
const PRODUCT_BUTTON_CONTAINS = 'Delete';
const PRODUCT_NAME = 'product-name';

beforeEach(() => wrapper = mount(DeleteProductModal, {
    propsData: {
        productInfos: [new Product(-1, PRODUCT_NAME, 0, 'description')],
    }
}));

it('creates a proper page structure', () => {
    expect(wrapper.element.children.length,
        `main page starting with ${wrapper.element.tagName} has no child elements`)
        .toBeGreaterThan(0);
});

it('has delete button', () => {
    // find the delete button to click
    const confirmButton = wrapper.findAll('button')
        .find(button => button.element.innerHTML?.toLowerCase().includes(PRODUCT_BUTTON_CONTAINS.toLowerCase()));

    console.log(confirmButton);

    expect(confirmButton?.exists(),
        `missing delete product button`)
        .toBe(true);

    expect(confirmButton.element.disabled,
        `Delete product button should always be enabled!`)
        .toBeFalsy();
});

it('correct modal title', () => {
    // find the modal title
    const modalTitle = wrapper.findAll('h3')
        .find(th => th.element.innerHTML?.toLowerCase().includes(MODAL_TITLE_CONTAINS.toLowerCase()));

    expect(modalTitle?.exists(),
        `missing or incorrect modal title`)
        .toBe(true);
});
