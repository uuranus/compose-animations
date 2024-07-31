# compose-animations
A collection of animations from popular apps like Instagram, YouTube, and Twitter with Jetpack Compose

# Instagram
<table>
  <th>pc upload complete</th>
  <th>heart</th>

  <tr>
    <td><img src = "https://github.com/user-attachments/assets/e9e452e5-3116-45b3-a701-6fbf08e9f463" width = "250"></td>
    <td><img src = "https://github.com/user-attachments/assets/322e50d7-97a8-451b-ab1c-126bdb886cdd" width = "250"></td>
  </tr>

  <tr>
<td>
      
``` kotlin
InstagramProgress(
    modifier = Modifier
        .fillMaxSize()
        .padding(32.dp),
    isComplete = true
)
```
</td>

<td>
  
```kotlin
InstagramLikeButton(
    modifier = Modifier
        .fillMaxSize()
        .padding(32.dp),
    isLiked = isLiked,
    onClick = {
        isLiked = !isLiked
    }
)
```

</td>
  </tr>
  
</table>

# Youtube

<table>
  <th>shimmering</th>
  
  <tr>
    <td align="center"><img src = "https://github.com/user-attachments/assets/3d35b704-d387-41b2-aa47-e17d447b314b" width = "250"></td>
  </tr>

  <tr>
<td>
      
``` kotlin
ShimmeringPlaceholder(
    modifier = Modifier
        .fillMaxWidth()
        .height(20.dp),
    backgroundColor = backgroundColor
    // !! it's the background color of
    // placeholder's external composable
)
```
</td>


  </tr>
  
</table>

# X (Twitter)

<table>
  <th>heart</th>
  
  <tr>
    <td align="center"><img src = "https://github.com/user-attachments/assets/a9a1d95f-45d4-4921-a5c7-88b9992d348b" width = "250"></td>
  </tr>

  <tr>
<td>
      
``` kotlin
TwitterLikeButton(
    modifier = Modifier.size(50.dp),
    isLiked = isLiked
) {
    isLiked = !isLiked
}
```
</td>

  </tr>
  
</table>
