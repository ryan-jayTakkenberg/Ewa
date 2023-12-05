/** @type {import('tailwindcss').Config} */
module.exports = {
    content: ['./index.html', './src/**/*.{vue,js,ts,jsx,tsx}'],
    theme: {
        extend: {},
    },
    // safelist: [
    //     {
    //         pattern: /./, // makes sure we have all the tailwind classes available (for development purposes)
    //     },
    // ],
    plugins: [],
}