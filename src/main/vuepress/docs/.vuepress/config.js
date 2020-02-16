module.exports = {
  title: 'pablosaraiva.com',
  description: 'Pablo Saraiva, programmer.',

  plugins: [
    ['@vuepress/blog', {
      sitemap: {
        hostname: 'https://pablosaraiva.com'
      },
      directories: [
        {
          id: 'posts',
          dirname: '_posts',
          path: '/posts/',
          pagination: {
            lengthPerPage: 2,
          }
        },
      ],
    }]
  ],

  themeConfig: {
    displayAllHeaders: true,
    nav: [
      {text: 'Home', link: '/'},
      {text: 'Blog', link: '/blog/'},
      {text: 'Music', link: '/music/'},
      {text: 'About', link: '/about/'}
    ]
  }
}