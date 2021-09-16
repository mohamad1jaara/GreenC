<template>
  <div id="Task">
    <h2>Choose a Task</h2>
    <!--
    - type="radio" for radio button
    - name="task" specifies the group
    - value not visible for user, goes to server
    - The "for" attribute of <label> must be equal to the id attribute of the related element to bind them together.
    -->
    <input type="radio" id="evaluate" name="task" value="evaluate" @click="$emit('sendTask','evaluate')">
    <label for="evaluate">Evaluate a configuration</label><br>
    <input type="radio" id="compare" name="task" value="compare" @click="$emit('sendTask','compare')">
    <label for="compare">Compare two configurations</label>
  </div>
</template>

<script>
export default {
  name: "Task"
}
</script>

<style scoped>
#Task{
  text-align: left;
  margin: 2%;
}


</style>