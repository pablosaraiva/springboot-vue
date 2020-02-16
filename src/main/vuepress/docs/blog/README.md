# Blog

<span v-for="page in $site.pages">
  <p v-if="page.id === 'posts'" href="">
    <a v-bind:href="page.path"><h2>{{ page.title }}</h2></a>
    <h4>{{page.frontmatter.description}}</h4>
  </p>
  
</span>

