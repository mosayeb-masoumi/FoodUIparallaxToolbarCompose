package com.example.fooduicompose
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fooduicompose.data.Recipe
import com.example.fooduicompose.data.strawberryCake
import com.example.fooduicompose.ui.theme.DarkGray
import com.example.fooduicompose.ui.theme.FoodUIcomposeTheme
import com.example.fooduicompose.ui.theme.Gray
import com.example.fooduicompose.ui.theme.Pink
import com.example.fooduicompose.ui.theme.Shapes
import com.example.fooduicompose.ui.theme.Transparent
import com.example.fooduicompose.ui.theme.White
import kotlin.math.max
import kotlin.math.min


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FoodUIcomposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainFragment(strawberryCake)
                }
            }
        }
    }
}

@Composable
fun MainFragment(recipe: Recipe) {

    val scrollState = rememberLazyListState()
    Box {
        Content(recipe , scrollState)
        ParallaxToolbar(recipe ,scrollState)
    }
}


@Composable
fun ParallaxToolbar(recipe: Recipe , scrollState: LazyListState) {

    val imageHeight = AppBarExpendedHeight - AppBarCollapsedHeight

    val maxOffset =
        with(LocalDensity.current){ imageHeight.roundToPx() }
//        with(LocalDensity.current){ imageHeight.roundToPx() } - WindowInsets.systemBars.layoutInsets.top
    val offset = min(scrollState.firstVisibleItemScrollOffset , maxOffset)
    val offsetProgress  = max(0f , offset *3f - 2f * maxOffset)/maxOffset



    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(AppBarExpendedHeight)
            .offset{ IntOffset(x = 0 , y = -offset)}
            .background(color = Color.White),
    ) {

        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight)
                    .graphicsLayer {
                        alpha = 1f - offsetProgress
                    }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.strawberry_pie_1),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colorStops = arrayOf(
                                    Pair(0.4f, Transparent),
                                    Pair(1f, White)
                                )
                            )
                        )
                )

                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.Bottom,

                    ) {
                    Text(
                        text = recipe.category,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .clip(shape = Shapes.small)
                            .background(color = Color.LightGray)
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(AppBarCollapsedHeight),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = recipe.title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(horizontal = (16 + 28 * offsetProgress).dp)
                        .scale(1f - 0.25f * offsetProgress)
                )
            }
        }


    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(AppBarCollapsedHeight)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        CircularButton(R.drawable.ic_arrow_back) {

        }
        CircularButton(R.drawable.ic_favorite) {}
    }

}

@Composable
fun CircularButton(
    @DrawableRes iconResource: Int,
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    onClick: (String) -> Unit,

    ) {

    Button(
        modifier = Modifier
            .width(38.dp)
            .height(38.dp),
        contentPadding = PaddingValues(),
        shape = Shapes.small,
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.Red,
            containerColor = Gray
        ),
        onClick = {
            onClick("clicked")
        }) {
        Icon(painter = painterResource(id = iconResource), contentDescription = null)
    }
}


@Composable
fun Content(recipe: Recipe ,scrollState: LazyListState) {

    LazyColumn(contentPadding = PaddingValues(top = AppBarExpendedHeight) , state = scrollState) {
        item {
            BasicInfo(recipe)
            Description(recipe)
            ServingCalculator()
            IngredientsHeader()
            IngredientList(recipe)
            ShoppingListButton()
            Reviews(recipe)
            Images()
        }
    }
}

@Composable
fun Images() {
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.padding(16.dp)) {
        Image(
            painter = painterResource(id = R.drawable.strawberry_pie_2),
            contentDescription = null, modifier = Modifier
                .weight(1f)
                .clip(shape = Shapes.medium)
        )

        Spacer(modifier = Modifier.weight(0.1f))
        Image(
            painter = painterResource(id = R.drawable.strawberry_pie_3),
            contentDescription = null, modifier = Modifier
                .weight(1f)
                .clip(shape = Shapes.medium)
        )
    }
}

@Composable
fun Reviews(recipe: Recipe) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = "Reviews", fontWeight = FontWeight.Bold)
            Text(text = recipe.reviews, color = Color.DarkGray)
        }

        Button(
            onClick = { /*TODO*/ }, elevation = null, colors = ButtonDefaults.buttonColors(
                contentColor = Pink,
                containerColor = Transparent
            )
        ) {
            Row(horizontalArrangement = Arrangement.Center) {
                Text(text = "See All")
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_right),
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun ShoppingListButton() {
    Button(
        onClick = { /*TODO*/ }, elevation = null, shape = Shapes.large,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.LightGray,
            contentColor = Color.Black
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(50.dp)
    ) {
        Text(text = "Add to shopping list")
    }
}

@Composable
fun IngredientList(recipe: Recipe) {
    EasyGrid(nColumns = 3, items = recipe.ingredients) {
        IngredientCard(it.image, it.title, it.subtitle, Modifier) {
            //clicked
            var a = 5
        }
    }

}

@Composable
fun <T> EasyGrid(nColumns: Int, items: List<T>, content: @Composable (T) -> Unit) {
    Column(Modifier.padding(16.dp)) {
        for (i in items.indices step nColumns) {
            Row {
                for (j in 0 until nColumns) {
                    if (i + j < items.size) {
                        Box(
                            contentAlignment = Alignment.TopCenter,
                            modifier = Modifier.weight(1f)
                        ) {
                            content(items[i + j])
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f, fill = true))
                    }
                }
            }
        }
    }
}

@Composable
fun IngredientCard(
    @DrawableRes iconResource: Int,
    title: String,
    subTitle: String,
    modifier: Modifier,
    onClick: (String) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(bottom = 16.dp)
    ) {
        Card(
            shape = Shapes.large, modifier = Modifier
                .width(100.dp)
                .height(100.dp)
                .padding(bottom = 8.dp)
                .clickable {
                    onClick("clicked")
                }
        ) {
            Image(
                painter = painterResource(id = iconResource),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Text(
            text = title,
            modifier = Modifier.width(100.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = title,
            color = DarkGray,
            modifier = Modifier.width(100.dp),
            fontSize = 14.sp,
        )
    }
}

@Composable
fun IngredientsHeader() {

    var btn1Activation by remember { mutableStateOf(true) }
    var btn2Activation by remember { mutableStateOf(false) }
    var btn3Activation by remember { mutableStateOf(false) }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(44.dp)
            .clip(Shapes.medium)
            .background(Color.LightGray)
            .padding(horizontal = 4.dp, vertical = 4.dp)
    ) {
        TabButton(onClick = {
            btn1Activation = true
            btn2Activation = false
            btn3Activation = false
        }, "Ingredients", btn1Activation, Modifier.weight(1f))
        TabButton(onClick = {
            btn1Activation = false
            btn2Activation = true
            btn3Activation = false
        }, "Tools", btn2Activation, Modifier.weight(1f))
        TabButton(onClick = {
            btn1Activation = false
            btn2Activation = false
            btn3Activation = true
        }, "Steps", btn3Activation, Modifier.weight(1f))
    }
}

@Composable
fun TabButton(onClick: (String) -> Unit, text: String, active: Boolean, modifier: Modifier) {
    Button(

        onClick = { onClick("clicked") }, shape = Shapes.medium,
        modifier = modifier.fillMaxHeight(),
        colors = if (active) ButtonDefaults.buttonColors(
            contentColor = White,
            containerColor = Pink
        ) else ButtonDefaults.buttonColors(
            contentColor = Pink,
            containerColor = Transparent
        ),
    ) {
        Text(text = text)
    }
}

@Composable
fun ServingCalculator() {

    var value by remember { mutableStateOf(6) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(Shapes.medium)
            .background(color = Color.LightGray)
            .padding(horizontal = 16.dp)
    ) {

        Text(text = "Serving", modifier = Modifier.weight(1f))
        CircularButton(iconResource = R.drawable.ic_minus, onClick = {
            value--
        })
        Text(text = "$value", modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp))
        CircularButton(iconResource = R.drawable.ic_plus, onClick = {
            value++
        })

    }
}

@Composable
fun Description(recipe: Recipe) {
    Text(
        text = recipe.description,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
fun BasicInfo(recipe: Recipe) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        InfoColumn(R.drawable.ic_clock, recipe.cookingTime)
        InfoColumn(R.drawable.ic_flame, recipe.energy)
        InfoColumn(R.drawable.ic_star, recipe.rating)
    }
}

@Composable
fun InfoColumn(@DrawableRes resourceIcon: Int, text: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            painter = painterResource(id = resourceIcon),
            contentDescription = null,
            tint = Pink,
            modifier = Modifier.height(24.dp)
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(text = text, fontWeight = FontWeight.Bold)
    }
}


@Preview(showBackground = true, widthDp = 380, heightDp = 1600)
@Composable
fun GreetingPreview() {
    FoodUIcomposeTheme {
        MainFragment(strawberryCake)
    }
}