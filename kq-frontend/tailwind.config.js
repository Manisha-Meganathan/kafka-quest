/** @type {import('tailwindcss').Config} */

module.exports = {
  content: [
    "./src/**/*.{html,js,jsx,ts,tsx}",
  ],
  theme: {
    safelist: [
      {
        pattern:
          /(bg|text|border)-primary-(black|grey|blue|border)/,
      },
    ],
    extend: {
      colors: {
        'primary-black': '#1D1D28',
        'primary-grey': '#383842',
        'primary-border': '#44444F',
        'primary-blue': '#274DD4',
        'grey-light': '#2B2B36'
      },
      width: {
        'popup-width': '312px'
      },
      height: {
        'text-input': '44px'
      },
      borderColor: {
        'default-border-color': '#44444F',
        'light-gray': '#44444F'
      },
      borderWidth: {
        'default-border-width': '1px'
      },
      borderRadius: {
        'default-border-radius': '5px'
      },
      padding: {
        'popup-padding': '20px',
        'button-padding-top': '10px'
      },
      margin: {
        'default-margin': '10px',
        '-top-15': '-15px'
      },
      fontFamily: {
        'sans': ['"Noto Sans"'],
      },
      fontSize: {
        'title-64' : ['64px', '87.17px'],
        'title-24' : ['24px', '32.69px'],
        'title-16' : ['16px','21.79px'],
        'subtitle-14' : ['14px', '19.07px']
      }
    },
  },
  plugins: [],
}

